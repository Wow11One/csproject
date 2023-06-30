import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import MyHeader from "./components/uicomponents/header.jsx";
import ProductTable from "./components/uicomponents/product-table/product-table.jsx";
import ProductPage from "./components/screens/home/product-page.jsx";
import {MyRouter} from "./components/router";
const App = () => {
  const [count, setCount] = useState(0)

  return (
      <MyRouter/>
      )
}

export default App
