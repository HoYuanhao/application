import { loginByUsername, logout, getUserInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    user: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    avatar: '',
    introduction: '',
    id: '',
    roles: [],
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_ID:(state, id)=>{
      state.id = id
    },
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(username, userInfo.password).then(response => {
          const data = response.data
          if(data.status==200){
          commit('SET_TOKEN', data.token)
          commit('SET_ID', data.uid)
          setToken(data.token)
          resolve()
        }else if(data.status==400){
          reject('用户名或密码错误!')
          commit('SET_ID', '0')
          resolve()
        }else {
          reject('服务器异常')
          commit('SET_ID', '1')
          resolve()
        }
        }).catch(error => {
          commit('SET_ID', '')
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
            if(response.data.status==200){
          const data = response.data.userInfo
          const role=['admin','editor']
          commit('SET_ROLES', role)
          commit('SET_NAME', data.userName)
          commit('SET_ID', data.uid)
          commit('SET_AVATAR', "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
          commit('SET_INTRODUCTION', "空")
        }else if(response.data.status==400){
          reject('没有权限')
        }else{
          reject('未知异常')
          commit('SET_ID', '')
        }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 动态修改权限
    ChangeRoles({ commit, dispatch }, role) {
      return new Promise(resolve => {
        commit('SET_TOKEN', role)
        setToken(role)
        getUserInfo(role).then(response => {
          const data = response.data.userInfo
          commit('SET_ROLES', role)
          commit('SET_NAME', data.userName)
          commit('SET_AVATAR', "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
          commit('SET_INTRODUCTION', "空")
          dispatch('GenerateRoutes', data) // 动态修改权限后 重绘侧边菜单
          resolve()
        })
      })
    }
  }
}

export default user
