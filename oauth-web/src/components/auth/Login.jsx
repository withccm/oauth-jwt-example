import React, {useMemo} from "react";
import GoogleLogin from "react-google-login";
import queryString from "query-string";
import {login} from "utils/ApiUtils";
import AuthUtils from "utils/AuthUtils";

const googleClientId = "구글 클라이언트 ID 설정은 readme 참고";

const Login = ({location}) => {
    const params = useMemo(() => {
        const queries = queryString.parse(location.search);
        return {
            redirect_uri: queries.redirect_uri ? decodeURIComponent(queries.redirect_uri) : '/'
        };
    }, [location]);

    if (AuthUtils.getToken()) {
        window.location.replace('//' + window.location.host + params.redirect_uri)
    }

    const onSuccessGoogle = async(response) => {
        const loginRes = await login(response.tokenId, 'google')
        if (loginRes.error) {
            alert('로그인에 실패했습니다.')
        } else {
            window.location.replace('//' + window.location.host + params.redirect_uri)
        }
    }

    const onFailureGoogle = (error) => {
        console.log(error);
        alert('로그인에 실패했습니다.')
    }

    return (<React.Fragment>
        로그인
        <ul>
            <li>
                <GoogleLogin
                    clientId={googleClientId}
                    responseType={"id_token"}
                    onSuccess={onSuccessGoogle}
                    onFailure={onFailureGoogle}/>
            </li>
        </ul>
    </React.Fragment>)
}

export default Login