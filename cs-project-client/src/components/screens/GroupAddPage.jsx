import Header from "../uicomponents/header.jsx";
import {useEffect, useState} from "react";

const GroupAddPage = () =>
{

    const emptyFormFields = {
        name:'',
        description:'',
    }
    const [formFields, setFormFields] = useState(emptyFormFields);

    const createGroup = async  (e) => {
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
                    id:-1
                }
            const response = await fetch('http://localhost:8765/api/group',{
                method : 'post',
                body :JSON.stringify(newGroup)
            })
            if(response.status === 200){
                alert("Group succesfully added");
                setFormFields(emptyFormFields);
            }
            else if(response.status === 400)
                alert('Group is not added: name is already used')
            else
                alert('Error')
        }
        catch (e) {
            alert(e.message)
        }

    }

    return(<>
            <Header/>
            <h2>Create new group</h2>
            <form>
                <label>
                    Name
                    <input onChange={e => setFormFields(prevState => ({...prevState, name:e.target.value}))} value={formFields.name} name="name" placeholder="Write product name" className="textbox" required/>
                </label>
                <label>Description
                    <textarea onChange={e => setFormFields(prevState => ({...prevState, description:e.target.value}))} value={formFields.description} rows="4" cols="50" name="subject" placeholder="Write product description" className="message" required></textarea>
                </label>
                <input onClick={e => createGroup(e)} name="submit"  type="submit" value="Create"/>
            </form>
        </>
    )
}
export default GroupAddPage;