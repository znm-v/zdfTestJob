import React, { useState, useMemo } from 'react';
import { View, Text, Image, ScrollView } from '@tarojs/components';
import { CATEGORIES } from '../../constants';
import { Page } from '../../types';
import './index.css';

const MenuPage = ({ menu, onNavigate, onAddToCart }) => {
  const [activeCategory, setActiveCategory] = useState('热门');

  const filteredMenu = useMemo(() => {
    if (activeCategory === '热门') {
      return menu.filter(item => item.category === '热门');
    }
    return menu.filter(item => item.category === activeCategory);
  }, [menu, activeCategory]);

  const handleAddToCart = (e, item) => {
    e.stopPropagation();
    onAddToCart(item);
  };

  return (
    <View className='menu-page'>
      <ScrollView scrollX className='category-scroll'>
        <View className='category-list'>
          {CATEGORIES.map(cat => (
            <View
              key={cat}
              className={`category-item ${activeCategory === cat ? 'active' : ''}`}
              onClick={() => setActiveCategory(cat)}
            >
              <Text>{cat}</Text>
            </View>
          ))}
        </View>
      </ScrollView>

      <View className='menu-list'>
        {(activeCategory === '热门' ? menu : filteredMenu).map(item => (
          <View
            key={item.id}
            className='menu-item'
            onClick={() => onNavigate(Page.DETAIL, item)}
          >
            <Image className='menu-item-image' src={item.img} mode='aspectFill' />
            <View className='menu-item-content'>
              <View>
                <Text className='menu-item-name'>{item.name}</Text>
                <Text className='menu-item-desc'>{item.desc}</Text>
              </View>
              <View className='menu-item-footer'>
                <Text className='menu-item-points'>
                  {item.points}
                  <Text className='menu-item-points-unit'>积分</Text>
                </Text>
                <View
                  className='add-btn'
                  onClick={(e) => handleAddToCart(e, item)}
                >
                  <Text className='add-btn-icon'>+</Text>
                </View>
              </View>
            </View>
          </View>
        ))}
      </View>
    </View>
  );
};

export default MenuPage;
