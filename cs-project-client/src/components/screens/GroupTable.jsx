import {useEffect, useState} from "react";
import GroupTableRow from "./GroupTableRow.jsx";
const GroupTable = () =>
{
    const [groups,setGroups] = useState([]);
    const [totalAmount,setTotalAmount] = useState(0);
    const [totalPrice,setTotalPrice] = useState(0);

    const fetchData = async () =>{
        const response = await fetch('http://localhost:8765/api/group');
        const data = await response.json();
        console.log(data);
        setGroups( data);
        let price = 0,amount = 0;
        data.forEach(d => price += d.price)
        data.forEach(d => amount += d.amount)
        setTotalPrice(price);
        setTotalAmount(amount);

    }
    useEffect(() => {
        fetchData()
    },[])

    return(<>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Product amount</th>
                <th>Product price</th>
                <th>Update</th>
                <th>Delete</th>
                <th>Products</th>
            </tr>
            <GroupTableRow setTotalAmount = {setTotalAmount} setTotalPrice = {setTotalPrice}  setGroups = {setGroups} groups = {groups}/>
            <tr>
                <td></td>
                <td className={'total'}>Total</td>
                <td></td>
                <td className={'total'}>{totalAmount}</td>
                <td className={'total'}>{totalPrice}</td>
                <td></td>
                <td></td>
            </tr>
        </table></>);
}
export default GroupTable