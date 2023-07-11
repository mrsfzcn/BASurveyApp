import axios from "axios";

const LOGIN = "http://localhost:8090/api/v1/auth/authenticate";
const VERIFYCODE = "http://localhost:8090/api/v1/auth/verifycode";

class AuthService {
  login(user) {
    return axios.post(LOGIN, user);
  }
  verifyCode(code) {
    return axios.post(VERIFYCODE, code);
  }
}

export default new AuthService();
