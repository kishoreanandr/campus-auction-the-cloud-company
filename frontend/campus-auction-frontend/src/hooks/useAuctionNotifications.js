import { useEffect, useRef, useState } from "react";
import {
  getBidHistory,
  getListing,
  getListings,
  getMyListings,
} from "../services/api";

const POLL_INTERVAL_MS = 30_000;

function getHighestBid(bids) {
  return bids.reduce((highest, bid) => {
    if (!highest || Number(bid.amount) > Number(highest.amount)) {
      return bid;
    }
    return highest;
  }, null);
}

export function useAuctionNotifications(user) {
  const [notifications, setNotifications] = useState([]);
  const [permission, setPermission] = useState(() =>
    "Notification" in window ? Notification.permission : "unsupported",
  );
  const previousSnapshot = useRef(null);
  const polling = useRef(false);

  useEffect(() => {
    if (!user || !["USER", "BIDDER"].includes(user.role)) {
      previousSnapshot.current = null;
      return undefined;
    }

    let active = true;
    let pollFailureReported = false;

    async function poll() {
      if (polling.current) {
        return;
      }

      polling.current = true;
      try {
        const [activeListings, myListings] = await Promise.all([
          getListings(),
          user.role === "BIDDER" ? getMyListings() : Promise.resolve([]),
        ]);
        const listingIds = new Set(
          [...activeListings, ...myListings].map((listing) => listing.id),
        );
        for (const [listingId, previous] of previousSnapshot.current || []) {
          if (
            previous.listing.sellerId === user.id
            || previous.bids.some((bid) => bid.bidderId === user.id)
          ) {
            listingIds.add(listingId);
          }
        }
        const currentListings = await Promise.all(
          [...listingIds].map((listingId) => getListing(listingId)),
        );
        const currentSnapshot = new Map();

        await Promise.all(
          currentListings.map(async (listing) => {
            const bids = await getBidHistory(listing.id);
            currentSnapshot.set(listing.id, {
              listing,
              bids,
              bidIds: new Set(bids.map((bid) => bid.id)),
              highestBid: getHighestBid(bids),
            });
          }),
        );

        if (!active) {
          return;
        }

        const oldSnapshot = previousSnapshot.current;
        if (oldSnapshot) {
          for (const [listingId, current] of currentSnapshot) {
            const previous = oldSnapshot.get(listingId);
            if (!previous) {
              continue;
            }

            if (current.listing.sellerId === user.id) {
              const newBids = current.bids.filter(
                (bid) => !previous.bidIds.has(bid.id),
              );
              for (const bid of newBids) {
                publish(
                  `New bid on ${current.listing.title}`,
                  `${bid.bidderName} bid ₹${bid.amount}.`,
                );
              }
            }

            const hadBid = previous.bids.some(
              (bid) => bid.bidderId === user.id,
            );
            if (
              user.role === "USER"
              && hadBid
              && previous.highestBid?.bidderId === user.id
              && current.highestBid
              && current.highestBid.bidderId !== user.id
            ) {
              publish(
                `You've been outbid on ${current.listing.title}`,
                `The new highest bid is ₹${current.highestBid.amount}.`,
              );
            }

            if (
              current.listing.status !== previous.listing.status
              && (
                current.listing.sellerId === user.id
                || (
                  user.role === "USER"
                  && current.bids.some((bid) => bid.bidderId === user.id)
                )
              )
            ) {
              publish(
                `Auction update: ${current.listing.title}`,
                `The auction status changed to ${current.listing.status.replaceAll("_", " ").toLowerCase()}.`,
              );
            }
          }
        }

        previousSnapshot.current = currentSnapshot;
        pollFailureReported = false;
      } catch (error) {
        if (active && !pollFailureReported) {
          publish("Notifications paused", error.message);
          pollFailureReported = true;
        }
      } finally {
        polling.current = false;
      }
    }

    function publish(title, body) {
      const id = `${Date.now()}-${Math.random()}`;
      setNotifications((previous) => [
        { id, title, body },
        ...previous,
      ].slice(0, 5));

      if (
        document.hidden
        && "Notification" in window
        && Notification.permission === "granted"
      ) {
        try {
          const notification = new Notification(title, { body });
          notification.onclick = () => window.focus();
        } catch (error) {
          console.error("Could not display browser notification", error);
        }
      }
    }

    void poll();
    const intervalId = window.setInterval(poll, POLL_INTERVAL_MS);

    return () => {
      active = false;
      window.clearInterval(intervalId);
      previousSnapshot.current = null;
    };
  }, [user]);

  async function enableNotifications() {
    if (!("Notification" in window)) {
      setPermission("unsupported");
      return "unsupported";
    }

    const result = await Notification.requestPermission();
    setPermission(result);
    return result;
  }

  function dismissNotification(id) {
    setNotifications((previous) =>
      previous.filter((notification) => notification.id !== id),
    );
  }

  return {
    notifications,
    permission,
    enableNotifications,
    dismissNotification,
  };
}
