// 页面枚举
export const Page = {
  SPLASH: 'splash',
  HOME: 'home',
  MENU: 'menu',
  DETAIL: 'detail',
  CART: 'cart',
  SHOP: 'shop',
  PROFILE: 'profile',
  ORDERS: 'orders',
  ADMIN: 'admin'
};

// 初始菜单数据
export const INITIAL_MENU = [
  {
    id: 1,
    name: '蜜汁烤鸡翅',
    points: 280,
    desc: '外酥里嫩，甜而不腻，情侣必点',
    img: 'https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=400',
    category: '热门'
  },
  {
    id: 2,
    name: '草莓奶昔',
    points: 180,
    desc: '新鲜草莓+香浓牛奶，甜蜜满分',
    img: 'https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400',
    category: '饮品'
  },
  {
    id: 3,
    name: '意式千层面',
    points: 320,
    desc: '层层叠叠的爱意，浓郁芝士香',
    img: 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=400',
    category: '主食'
  },
  {
    id: 4,
    name: '提拉米苏',
    points: 240,
    desc: '经典意式甜品，带你去意大利',
    img: 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=400',
    category: '甜品'
  }
];

// 初始商店数据
export const INITIAL_SHOP = [
  {
    id: 101,
    name: '情侣马克杯套装',
    points: 1200,
    img: 'https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=400'
  },
  {
    id: 102,
    name: '定制相册',
    points: 1800,
    img: 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400'
  },
  {
    id: 103,
    name: '香薰蜡烛礼盒',
    points: 980,
    img: 'https://images.unsplash.com/photo-1602874801006-e04b6d0c5d8f?w=400'
  }
];

// 分类列表
export const CATEGORIES = ['热门', '主食', '饮品', '甜品'];

// 口味选项
export const FLAVORS = ['原味', '浓郁味'];
