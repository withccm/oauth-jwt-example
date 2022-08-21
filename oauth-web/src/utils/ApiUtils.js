import axios from "axios";
import AuthUtils from "utils/AuthUtils";

export const goLogin = () => {
    setTimeout(() => {
        AuthUtils.removeAuth()
        const redirect_uri = encodeURIComponent(window.location.pathname + window.location.search)
        window.location.replace('http://' + window.location.host + '/login?redirect_uri=' + redirect_uri);
    }, 50)
}

export const createApiClient = () => {
    const client = axios.create({
        baseURL: process.env.REACT_APP_API_URL,
        timeout: 4000,
        withCredentials: true,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json; charset=UTF-8'
        },
    });

    client.interceptors.request.use((config) => {
        const token = AuthUtils.getToken()
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    });

    client.interceptors.response.use(
        (response) => {
            return response.data;
        },
        async (error) => {
            if (!error.response) {  // timeout
                return Promise.reject(error);
            }

            if (error.response.status === 401) {
                if (AuthUtils.getToken() && AuthUtils.getRefreshToken()) {
                    // refresh 요청
                    const refreshTokenRes = await refreshToken()
                    if (refreshTokenRes.error) {
                    } else {
                        AuthUtils.setToken(refreshTokenRes.data.data)
                        return client.request(error.config)
                    }
                }
                alert('로그인이 필요합니다.')
                goLogin()
            }

            return Promise.reject(error);
        }
    );

    return client;
}

export const apiIClient = createApiClient()

const refreshToken = async () => {
    const token = AuthUtils.getToken()
    return await axios.post('/api/v1/auth/refresh', {
        refreshToken : AuthUtils.getRefreshToken()
    }, {
        headers: {
            'Authorization': 'Bearer ' + token,
        }
    }).then(response => {
        return response
    }).catch(error => {
        console.log('refresh error', error)
        return error.response.data
    })
}

export const login = async (tokenId, type) => {
    return await axios.post('/api/v1/auth/login/' + type, {
        accessToken : tokenId
    }, {
    }).then(response => {
        AuthUtils.setToken(response.data.data)
        return response
    }).catch(error => {
        console.log(error)
        return error.response.data
    })
}