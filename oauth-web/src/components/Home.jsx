import React, {useContext, useEffect, useState} from "react";

import {apiIClient} from "utils/ApiUtils";
import Auth from "components/fragment/Auth";
import Menu from "components/fragment/Menu";
import AuthUtils from "../utils/AuthUtils";
import {AppStoreContext} from "../App";

const Home = () => {

    const [books, setBooks] = useState([])
    const {myProfile} = useContext(AppStoreContext)

    useEffect(()=> {
        if (!AuthUtils.getToken()) {
            return
        }
        apiIClient.get("/api/v1/books").then(response => {
            setBooks(response.data)
        }).catch(error => {
            console.log('book error', error)
        })
    }, [myProfile])

    return (<div>
        <Auth/>
        <Menu/>
        <h3>홈</h3>

        <h4>책 목록</h4>
        {
            books && <ul>
                {books.map((each, i) =>
                    <li key={each.id}>{each.bookname}</li>
                )}
            </ul>
        }
        {
            !AuthUtils.getToken() && <div>로그인이 필요합니다</div>
        }
    </div>)
}

export default Home