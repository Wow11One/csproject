import MyHeader from "../uicomponents/header.jsx";
import ProductTable from "../uicomponents/product-table/product-table.jsx";
import {useEffect, useState} from "react";
import {instance} from "eslint-plugin-react/lib/util/lifecycleMethods.js";
import ProductByGroupTable from "./ProductsByGroupTable.jsx";

const ProductsByGroupPage = () =>
{
    return(
        <>
            <MyHeader/>
            <h2>Goods</h2>
            <hr/>
            <div className={'main'}>
                <button className={'add-group-product'} onClick= {e => window.location.href = '/products/add'}>
                    Add new product
                </button>


            </div>
            <ProductByGroupTable/>
        </>
    )
}
export default ProductsByGroupPage