import axios from "axios";

const UPDATE_TAG = "http://localhost:8090/api/v1/maintags/tagname";

class TagService {
  async updateTag(updateTag) {
    return await axios.put(UPDATE_TAG, updateTag, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
  }
}

export default new TagService();
