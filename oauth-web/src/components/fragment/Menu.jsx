import {Link} from "react-router-dom";
import React from "react";

const Menu = () => {
    return (<ul>
        <li><Link to='/' >home</Link></li>
        <li><Link to='/profile' >profile</Link></li>
    </ul>)
}

export default Menu