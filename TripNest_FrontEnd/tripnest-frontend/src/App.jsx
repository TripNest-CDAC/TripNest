import Footer from "./components/Footer"
import Navbar from "./components/navbar"
import Home from "./pages/Home"

function App() {

  return (
    <>
    <div className="d-flex flex-column min-vh-100">
    <Navbar/>

      <main className="flex-grow-1">
        <Home />
      </main>

      {/* Later we will use React Router */}

      {/* <Login /> */}

      {/* <RegisterUser /> */}

      {/* <RegisterCompany /> */}

      {/* <ApproveCompany /> */}

      <Footer />

      </div>
      
    </>
  )
}

export default App
