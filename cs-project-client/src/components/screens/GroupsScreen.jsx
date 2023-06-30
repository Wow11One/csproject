import MyHeader from "../uicomponents/header.jsx";
import ProductTable from "../uicomponents/product-table/product-table.jsx";
import GroupTable from "./GroupTable.jsx";

const GroupPage = () =>
{
    return(
        <>
            <MyHeader/>
            <h2>Groups</h2>
            <hr/>
            <div className={'main'}>
                <button onClick= {e => window.location.href = '/groups/add'}>
                    Add new group
                </button>
            </div>
            <GroupTable/>
        </>
    )
}
export default GroupPage