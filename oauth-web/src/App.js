import axios from "axios";
import React, {useState} from "react";

import {BrowserRouter, Route} from "react-router-dom";

import Home from "components/Home";
import Profile from "components/Profile";
import Login from "components/auth/Login";


axios.defaults.baseURL = process.env.REACT_APP_API_URL;

export const AppStoreContext = React.createContext()

const App = () => {
  const [myProfile, setMyProfile] = useState()

    return (
    <AppStoreContext.Provider value={{myProfile: myProfile, setMyProfile: setMyProfile}}>
      <BrowserRouter>
          <Route exact path="/" component={Home} />
          <Route exact path="/profile" component={Profile} />
          <Route exact path="/login" component={Login} />
      </BrowserRouter>
    </AppStoreContext.Provider>
  );
}

export default App;
