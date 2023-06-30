import styles from  './header-styles/header.module.css';

const MyHeader = () =>{

    return (<header>
        <div className={styles.header_name}>Shop</div>
        <nav>
            <a href={'/products'}>Goods</a>
            <a href={'/groups'}>Groups</a>
        </nav>
            </header>)
}
export default MyHeader;