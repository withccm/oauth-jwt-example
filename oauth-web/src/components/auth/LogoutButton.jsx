import React, {useContext} from "react";
import {apiIClient} from "utils/ApiUtils";
import AuthUtils from "utils/AuthUtils";
import {AppStoreContext} from "App";

const LogoutButton = () => {

    const {setMyProfile} = useContext(AppStoreContext)

    const onClickLogout = () => {
        apiIClient.post("/api/v1/logout",{},{})
        .then(response => {
            console.log('logout')
        }).catch(error => {
            console.log('logout error', error)
        }).finally(() => {
            AuthUtils.removeAuth()
            setMyProfile(null)
        })
    }

    return (<React.Fragment>
        {
            AuthUtils.getToken() && <button onClick={onClickLogout}>로그아웃</button>
        }
    </React.Fragment>)
}

export default LogoutButton