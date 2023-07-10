import { useState } from 'react'
import reactLogo from './assets/react.svg'

import './App.css'
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import Login from './pages/LoginPage/login';


function App() {
  const [count, setCount] = useState(0)

  return (
    <Router>
    <Routes>

        <Route path="/login" element={<Login />} />
        <Route path="/*" element={<Navigate to="/"/>} />
    </Routes>
    </Router>
  )
}

export default App
