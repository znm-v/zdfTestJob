/**
 * 项目重构后的清理脚本
 * 用于移除根目录的冗余文件
 */

const fs = require('fs');
const path = require('path');

// 需要清理的文件列表（根目录）
const filesToRemove = [
  'App.jsx',
  'index.jsx',
  'constants.jsx',
  'types.js',
  'index.html',
  'index.css',
  'vite.config.js'
];

// 需要清理的目录
const dirsToRemove = [];

console.log('🧹 开始清理项目冗余文件...\n');

// 清理文件
filesToRemove.forEach(file => {
  const filePath = path.join(process.cwd(), file);
  
  if (fs.existsSync(filePath)) {
    try {
      fs.unlinkSync(filePath);
      console.log(`✅ 已删除: ${file}`);
    } catch (error) {
      console.error(`❌ 删除失败: ${file}`, error.message);
    }
  } else {
    console.log(`⏭️  跳过（不存在）: ${file}`);
  }
});

// 清理目录
dirsToRemove.forEach(dir => {
  const dirPath = path.join(process.cwd(), dir);
  
  if (fs.existsSync(dirPath)) {
    try {
      fs.rmSync(dirPath, { recursive: true, force: true });
      console.log(`✅ 已删除目录: ${dir}`);
    } catch (error) {
      console.error(`❌ 删除目录失败: ${dir}`, error.message);
    }
  } else {
    console.log(`⏭️  跳过（不存在）: ${dir}`);
  }
});

console.log('\n✨ 清理完成！');
console.log('\n📝 提示：');
console.log('   - 如果需要保留某些文件，请手动恢复');
console.log('   - 建议在清理前先提交 git 或备份');
console.log('   - 运行 npm run dev:weapp 测试项目是否正常\n');
