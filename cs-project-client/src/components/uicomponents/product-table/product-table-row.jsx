import styles from "../table-styles/product-table.module.css";

const ProductTableRows = ({products,setProducts}) =>
{
    const deleteProduct = (id) =>
    {
        if(confirm("do you wanna delete this product?"))
        {
            setProducts(products.filter(product => product.id !== id));
            deleteRequest(id);
        }
    }

    const deleteRequest = async (id) =>
    {
        const response = await fetch(`http://localhost:8765/api/good/${id}`,{
            method:'delete'
        }).catch(err => console.log(err))
    }
    const updateProduct = (event, product) =>
    {
        if(event.keyCode === 13){

            event.target.blur();
        }

    }
    const removeProduct = async (e,product) =>
    {
        let prompt = window.prompt('How many products do you want to remove?');
        try {
            const res = Number.parseInt(prompt);
            if(isNaN(res) || res < 0)
            {
                alert('incorrect value');
                return;
            }
            if(res < product.amount)
                product.amount -= res;
            else
                product.amount = 0;

            const response = await fetch(`http://localhost:8765/api/good/${product.id}`,{
                method : 'put',
                body :JSON.stringify(product)
            })
            if(response.status === 200){
                e.target.value = product.amount;
                alert("Product succesfully changed");
                window.location.href = '/';

            }
            else
                alert('Error')
        }
        catch (e)
        {
            alert(e.message)
        }
    }
    const addProduct = async (e,product) =>
    {
        let prompt = window.prompt('How many products do you want to add?');
        try {
            const res = Number.parseInt(prompt);
            if(isNaN(res) || res < 0)
            {
                alert('incorrect value');
                return;
            }
                product.amount += res;
            const response = await fetch(`http://localhost:8765/api/good/${product.id}`,{
                method : 'put',
                body :JSON.stringify(product)
            })
            if(response.status === 200){
                e.target.value = product.amount;
                alert("Product succesfully changed");
                window.location.href = '/';
            }
            else
                alert('Error')
        }
        catch (e)
        {
            alert(e.message)
        }
    }
    return(<>
        {products.map(product =>
            <tr id={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>{product.groupName}</td>
                <td>{product.description}</td>
                <td><button onClick={event => removeProduct(event,product)} className={'add-remove-product'}>-</button></td>
                <td>{product.amount}</td>
                <td><button onClick={event => addProduct(event,product)} className={'add-remove-product'}>+</button></td>
                <td>{product.price}</td>
                <td>{product.producer}</td>
                <td><button onClick={event => window.location.href = `/products/update/${product.id}`} >Update</button></td>
                <td><button onClick = {e => deleteProduct(product.id)}>Delete</button></td>
            </tr>
            )
        }
    </>)
}
export default ProductTableRows