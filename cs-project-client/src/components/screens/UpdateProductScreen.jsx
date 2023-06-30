import Header from "../uicomponents/header.jsx";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";

const UpdateProductScreen = () =>
{
    const {id} = useParams();
    const emptyFormFields = {
        name:'',
        description:'',
        price:'',
        amount:'',
        producer:'',
        group:''

    }
    const [formFields, setFormFields] = useState(emptyFormFields);
    const [groups,setGroups] = useState([]);
    const [product,setProduct] = useState({})

    useEffect(() => {
        const fetchData = async () =>{
            const response = await fetch('http://localhost:8765/api/group');
            const data = await response.json();
            setGroups(data)
        }
        const fetchProduct = async () =>{
            const response = await fetch(`http://localhost:8765/api/good/${id}`);
            const data = await response.json();
            if(response.status === 400){
                alert('incorrect id');
                return;
            }

            setFormFields({name:data.name,description: data.description, amount: `${data.amount}`, group: `${data.groupId}`, price: `${data.price}`, producer: data.producer});
            setProduct(data);
        }
        fetchData()
        fetchProduct()
    },[])

    const updateProduct = async  (e) => {
        e.preventDefault();
        const name = formFields.name;
        const description = formFields.description;
        const producer = formFields.producer;
        if(name.trim().length === 0 || producer.trim().length === 0){
            alert('empty fields');
            return;
        }
        try {
            const price = Number.parseInt(formFields.price);
            if(price < 0 || isNaN(price))
            {
                alert('price is not correct')
                return;
            }
            const amount = Number.parseInt(formFields.amount)
            if(amount < 0 || isNaN(amount))
            {
                alert('amount is not correct')
                return;
            }
            const groupId = Number.parseInt(formFields.group)
            const newProduct =
                {
                    name,
                    description,
                    producer,
                    price,
                    amount,
                    groupId,
                    id:product.id
                }
            console.log(newProduct);
            const response = await fetch(`http://localhost:8765/api/good/${id}`,{
                method : 'put',
                body :JSON.stringify(newProduct)
            })
            if(response.status === 200){
                alert("Product succesfully changed");
                window.location.href='/';
            }
            else if(response.status === 400)
                alert('Product is not changed: name is already used')
            else
                alert('Error')
        }
        catch (e) {
            alert(e.message)
        }

    }

    return(<>
            <Header/>
            <h2>Update "{product.name}"</h2>
            <form>
                <label>
                    Name
                    <input onChange={e => setFormFields(prevState => ({...prevState, name:e.target.value}))} value={formFields.name} name="name" placeholder="Write product name" className="textbox" required/>
                </label>
                <label>
                    Producer
                    <input onChange={e => setFormFields(prevState => ({...prevState, producer:e.target.value}))} value={formFields.producer} name="producer" placeholder="Write product producer" className="textbox"  required/>
                </label>
                <label>
                    Price
                    <input onChange={e => setFormFields(prevState => ({...prevState, price:e.target.value}))} value={formFields.price} name="price" placeholder="Write product price" className="textbox"  required/>
                </label>
                <label>
                    Group
                    <select onChange={e => setFormFields(prevState => ({...prevState, group:e.target.value}))} name = "groups">
                        <option disabled>Select product group</option>
                        {groups.map(group =>
                            <option selected = {group.id === product.groupId} value={group.id}>{group.name}</option>
                        )}
                    </select>
                </label>
                <label>Amount
                    <input onChange={e => setFormFields(prevState => ({...prevState, amount:e.target.value}))} value={formFields.amount} name="amount" placeholder="Write product amount" className="textbox" required/>
                </label>
                <label>Description
                    <textarea onChange={e => setFormFields(prevState => ({...prevState, description:e.target.value}))} value={formFields.description} rows="4" cols="50" name="subject" placeholder="Write product description" className="message" required></textarea>
                </label>
                <input onClick={e => updateProduct(e)} name="submit"  type="submit" value="Update"/>
            </form>
        </>
    )
}
export default UpdateProductScreen;