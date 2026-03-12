import { useState, useEffect } from 'react';
import { storageService } from '../services/storage';

export const usePoints = (initialPoints = 2480) => {
  const [points, setPoints] = useState(initialPoints);

  // 加载积分
  useEffect(() => {
    const loadPoints = async () => {
      const savedPoints = await storageService.getPoints();
      if (savedPoints > 0) {
        setPoints(savedPoints);
      }
    };
    loadPoints();
  }, []);

  // 保存积分
  useEffect(() => {
    storageService.setPoints(points);
  }, [points]);

  // 增加积分
  const addPoints = (amount) => {
    setPoints(prev => prev + amount);
  };

  // 扣除积分
  const deductPoints = (amount) => {
    if (points >= amount) {
      setPoints(prev => prev - amount);
      return true;
    }
    return false;
  };

  return {
    points,
    setPoints,
    addPoints,
    deductPoints
  };
};
