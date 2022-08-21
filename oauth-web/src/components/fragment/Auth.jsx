import LoginButton from "components/auth/LoginButton";
import React, {useContext, useEffect} from "react";
import LogoutButton from "components/auth/LogoutButton";
import {AppStoreContext} from "App";
import AuthUtils from "utils/AuthUtils";
import {apiIClient} from "utils/ApiUtils";

const Auth = () => {
    const {myProfile, setMyProfile} = useContext(AppStoreContext)

    useEffect(() => {
        if (AuthUtils.getToken() && !myProfile) {
            apiIClient.get('/api/v1/myProfile').then(response => {
                try {
                    if (response.data) {
                        setMyProfile(response.data)
                    }
                } catch (notFound) {
                    console.log('notFound user by token', notFound)
                }
            }).catch(error => {
                console.log('me error', error)
            })
        }
    }, [])

    return (<div>
        {myProfile && myProfile.name && <React.Fragment>
            {myProfile.name} 님
        </React.Fragment>}
        <LoginButton />
        <LogoutButton />
    </div>)
}

export default Auth