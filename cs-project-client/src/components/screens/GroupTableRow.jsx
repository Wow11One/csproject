import {useEffect, useState} from "react";

const GroupTableRows = ({setTotalAmount,setTotalPrice,groups,setGroups}) =>
{

    const deleteGroup = (group) =>
    {
        if(confirm("do you wanna delete this group?"))
        {
            setTotalPrice(prev => (prev - group.price));
            setTotalAmount(prev => (prev - group.amount));
            setGroups(groups.filter(product => product.id !== group.id));
            deleteRequest(group.id);
        }
    }

    const deleteRequest = async (id) =>
    {
        const response = await fetch(`http://localhost:8765/api/group/${id}`,{
            method:'delete'
        }).catch(err => console.log(err))
    }
    const updateProduct = (event, product) =>
    {
        if(event.keyCode === 13){

            event.target.blur();
        }

    }

    return(<>
        {groups.map(group =>
            <tr id={group.id}>
                <td>{group.id}</td>
                <td>{group.name}</td>
                <td>{group.description}</td>
                <td>{group.amount}</td>
                <td>{group.price}</td>
                <td><button onClick={event => window.location.href = `/groups/update/${group.id}`} >Update</button></td>
                <td><button onClick = {e => deleteGroup(group)}>Delete</button></td>
                <td><button onClick = {e => window.location.href = `/groups/${group.id}/products`}>→</button></td>
            </tr>
        )
        }
    </>)
}
export default GroupTableRows