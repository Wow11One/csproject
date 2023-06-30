import {BrowserRouter, Route, Routes} from "react-router-dom";
import Home from "./screens/home/product-page.jsx";
import ProductPage from "./screens/home/product-page.jsx";
import AddProductScreen from "./screens/AddProductScreen.jsx";
import UpdateProductScreen from "./screens/UpdateProductScreen.jsx";
import GroupPage from "./screens/GroupsScreen.jsx";
import GroupAddPage from "./screens/GroupAddPage.jsx";
import GroupUpdatePage from "./screens/UpdateGroupPage.jsx";
import ProductsByGroupPage from "./screens/ProductsByGroup.jsx";

export const MyRouter = () =>
{
    return (<BrowserRouter>
    <Routes>
        <Route element={<ProductPage/>} path='/'/>
        <Route element={<ProductPage/>} path='/products'/>
        <Route element={<GroupPage/>} path='/groups'/>
        <Route element={<ProductsByGroupPage/>} path='/groups/:id/products'/>
        <Route element={<GroupAddPage/>} path='/groups/add'/>
        <Route element={<GroupUpdatePage/>} path='/groups/update/:id'/>
        <Route element={<AddProductScreen/>} path='/products/add'/>
        <Route element={<UpdateProductScreen/>} path='/products/update/:id'/>
        <Route element={<div>Not Found</div>} path='*'/>
    </Routes>
    </BrowserRouter>
            )
}
