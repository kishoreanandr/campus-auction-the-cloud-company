import { useState } from "react";
import { login, register } from "../services/api";

function AuthPage({ onAuthenticated }) {
  const [mode, setMode] = useState("login");
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "USER",
  });
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  function handleChange(event) {
    const { name, value } = event.target;
    setFormData((previous) => ({ ...previous, [name]: value }));
    setError("");
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError("");

    try {
      const response =
        mode === "register"
          ? await register(formData)
          : await login({
              email: formData.email,
              password: formData.password,
            });
      onAuthenticated(response);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

  function changeMode(nextMode) {
    setMode(nextMode);
    setError("");
  }

  return (
    <section className="auth-page">
      <div className="form-container">
        <h1>{mode === "register" ? "Create your account" : "Welcome back"}</h1>
        <p>
          {mode === "register"
            ? "Create an account to bid on campus listings."
            : "Sign in to your Campus Auction account."}
        </p>

        <form className="listing-form" onSubmit={handleSubmit}>
          {mode === "register" && (
            <>
              <label htmlFor="auth-name">Name</label>
              <input
                id="auth-name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                maxLength={100}
                autoComplete="name"
                required
              />
            </>
          )}

          <label htmlFor="auth-email">Email</label>
          <input
            id="auth-email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            autoComplete="email"
            maxLength={254}
            required
          />

          <label htmlFor="auth-password">Password</label>
          <input
            id="auth-password"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            autoComplete={mode === "register" ? "new-password" : "current-password"}
            minLength={8}
            maxLength={72}
            required
          />

          {mode === "register" && (
            <>
              <label htmlFor="auth-role">Account type</label>
              <select
                id="auth-role"
                name="role"
                value={formData.role}
                onChange={handleChange}
              >
                <option value="USER">Student / bidder (places bids)</option>
                <option value="BIDDER">Seller / lister (posts auctions)</option>
              </select>
            </>
          )}

          {error && <p className="form-message form-error" role="alert">{error}</p>}

          <button
            type="submit"
            className="submit-listing-button"
            disabled={submitting}
          >
            {submitting
              ? "Please wait…"
              : mode === "register"
                ? "Register"
                : "Sign in"}
          </button>
        </form>

        <p className="auth-switch">
          {mode === "register" ? "Already registered?" : "New to Campus Auction?"}{" "}
          <button
            type="button"
            onClick={() => changeMode(mode === "register" ? "login" : "register")}
          >
            {mode === "register" ? "Sign in" : "Create an account"}
          </button>
        </p>
      </div>
    </section>
  );
}

export default AuthPage;
