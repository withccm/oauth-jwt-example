import React, {useContext, useEffect} from "react";
import Auth from "components/fragment/Auth";
import Menu from "components/fragment/Menu";
import AuthUtils from "../utils/AuthUtils";
import {goLogin} from "../utils/ApiUtils";
import {AppStoreContext} from "../App";

const Profile = () => {
    const {myProfile} = useContext(AppStoreContext)

    useEffect(()=> {
        if (!AuthUtils.getToken()) {
            alert('로그인이 필요합니다.')
            goLogin()
        }
    }, [])

    return (<div>
        <Auth/>
        <Menu/>
        <h3>프로필</h3>
        {
            myProfile && <ul>
                <li>{myProfile.name}</li>
                <li><img src={myProfile.imageUrl} alt='프로필 이미지' width={30} height={30} /></li>
                <li>{myProfile.oauthType}</li>
            </ul>

        }
    </div>)
}

export default Profile