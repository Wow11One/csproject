import Header from "../uicomponents/header.jsx";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";

const UpdateGroupPage = () =>
{
    const {id} = useParams();
    const emptyFormFields = {
        name:'',
        description:''
    }
    const [formFields, setFormFields] = useState(emptyFormFields);
    const [group,setGroup] = useState({})

    useEffect(() => {

        const fetchGroup = async () =>{
            const response = await fetch(`http://localhost:8765/api/group/${id}`);
            const data = await response.json();
            console.log(data);
            if(response.status === 400){
                alert('incorrect id');
                return;
            }

            setFormFields({name:data.name,description: data.description});
            setGroup(data);
        }
        fetchGroup()
    },[])

    const updateGroup = async  (e) => {
        e.preventDefault();
        const name = formFields.name;
        const description = formFields.description;

        if(name.trim().length === 0){
            alert('empty fields');
            return;
        }
        try {

            const newGroup =
                {
                    name,
                    description,
                    id:Number.parseInt(id)
                }
            const response = await fetch(`http://localhost:8765/api/group/${id}`,{
                method : 'put',
                body :JSON.stringify(newGroup)
            })
            if(response.status === 200){
                alert("Group succesfully changed");
                window.location.href='/groups';
            }
            else if(response.status === 400)
                alert('Group is not changed: name is already used')
            else
                alert('Error')
        }
        catch (e) {
            alert(e.message)
        }

    }

    return(<>
            <Header/>
            <h2>Update "{group.name}"</h2>
            <form>
                <label>
                    Name
                    <input onChange={e => setFormFields(prevState => ({...prevState, name:e.target.value}))} value={formFields.name} name="name" placeholder="Write product name" className="textbox" required/>
                </label>
                <label>Description
                    <textarea onChange={e => setFormFields(prevState => ({...prevState, description:e.target.value}))} value={formFields.description} rows="4" cols="50" name="subject" placeholder="Write product description" className="message" required></textarea>
                </label>
                <input onClick={e => updateGroup(e)} name="submit"  type="submit" value="Update"/>
            </form>
        </>
    )
}
export default UpdateGroupPage;