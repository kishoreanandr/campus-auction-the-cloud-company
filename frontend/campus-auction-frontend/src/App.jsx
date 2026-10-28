import { useState } from 'react'
import Navbar from "./components/Navbar";

import './App.css'

function App() {
    const [currentPage, setCurrentPage] = useState("home");

  return (
    <>
      <Navbar onNavigate={setCurrentPage} />

      <main className="page-content">
        <h1>
          {currentPage === "home" && "Browse Listings"}
          {currentPage === "my-listings" && "My Listings"}
          {currentPage === "create" && "Create a Listing"}
        </h1>
      </main>
    
    </>
  )
}

export default App;
