import React from "react";
import AuthUtils from "utils/AuthUtils";

const LoginButton = () => {

    const onClickLogin = () => {
        const redirect_uri = encodeURIComponent(window.location.pathname + window.location.search)
        window.location.href = '//' + window.location.host + '/login?redirect_uri=' + redirect_uri
    }

    return (<React.Fragment>
        {
            !AuthUtils.getToken() && <button onClick={onClickLogin}>로그인</button>
        }
    </React.Fragment>)
}

export default LoginButton