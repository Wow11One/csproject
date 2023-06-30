import Header from "../uicomponents/header.jsx";
import {useEffect, useState} from "react";

const AddProductScreen = () =>
{

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

    useEffect(() => {
        const fetchData = async () =>{
            const response = await fetch('http://localhost:8765/api/group');
            const data = await response.json();
            setGroups(data)
            let group = data[0]
            if(group)
            setFormFields(prevState => ({...prevState,group:`${group.id}`}))

        }
        fetchData()
    },[])

    const createProduct = async  (e) => {
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
                    id:-1
                }
                console.log(newProduct);
            const response = await fetch('http://localhost:8765/api/good',{
                method : 'post',
                body :JSON.stringify(newProduct)
            })
            if(response.status === 200){
                alert("Product succesfully added");
                emptyFormFields.group = `${groupId}`;
                setFormFields(emptyFormFields);
            }
            else if(response.status === 400)
                alert('Product is not added: name is already used')
            else
                alert('Error')
        }
        catch (e) {
            alert(e.message)
        }

    }

    return(<>
            <Header/>
            <h2>Create new good</h2>
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
                            <option value={group.id}>{group.name}</option>
                    )}
                </select>
                </label>
                <label>Amount
                <input onChange={e => setFormFields(prevState => ({...prevState, amount:e.target.value}))} value={formFields.amount} name="amount" placeholder="Write product amount" className="textbox" required/>
                </label>
                <label>Description
                <textarea onChange={e => setFormFields(prevState => ({...prevState, description:e.target.value}))} value={formFields.description} rows="4" cols="50" name="subject" placeholder="Write product description" className="message" required></textarea>
                </label>
                <input onClick={e => createProduct(e)} name="submit"  type="submit" value="Create"/>
            </form>
        </>
    )
}
export default AddProductScreen;