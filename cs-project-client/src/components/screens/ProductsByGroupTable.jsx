import styles from '../uicomponents/table-styles/product-table.module.css'
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import ProductTableRows from "../uicomponents/product-table/product-table-row.jsx";
const ProductByGroupTable = () =>
{
    const {id} = useParams();
    const [products,setProducts] = useState([]);
    const [searchText,setSearchText] = useState('');
    const fetchData = async () =>{
        const response = await fetch(`http://localhost:8765/api/products/groups/${id}`);
        const data = await response.json();
        console.log(data);
        setProducts(data)
        return data
    }
    useEffect(() => {
        fetchData()
    },[])

    const search = async  (text) =>{
        const prods = await fetchData()
        setProducts(prods.filter(product => product.name.toLowerCase().includes(text.toLowerCase())));
    }


    return(<>
        <div className={'search'}>
            <input onChange={event => setSearchText(event.target.value)} placeholder={'type product name'}/>
            <button onClick={event => search(searchText)}>Search</button>
        </div>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Group name</th>
                <th>Description</th>
                <th>-</th>
                <th>Amount</th>
                <th>+</th>
                <th>Price</th>
                <th>Producer</th>
                <th>Update</th>
                <th>Delete</th>
            </tr>
            <ProductTableRows setProducts={setProducts} products = {products}/>
        </table></>);
}
export default ProductByGroupTable