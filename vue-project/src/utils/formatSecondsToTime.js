/**
 * 秒数转化为 分:秒 或 时:分:秒 格式
 * @param {number} seconds - 总秒数（必须为非负整数）
 * @param {boolean} showHour - 是否显示小时（默认 false，仅分:秒）
 * @returns {string} 格式化后的时间字符串（如 "03:45"、"01:12:08"）
 */
export const formatSecondsToTime = (seconds, showHour = false) => {
  // 处理异常值（负数、非数字）
  if (isNaN(seconds) || seconds < 0) return "00:00";

  // 转换为整数（避免小数秒）
  const totalSeconds = Math.floor(seconds);

  let hour = 0;
  let minute = 0;
  let sec = 0;

  if (showHour) {
    // 计算小时、分钟、秒（适用于超过1小时的场景）
    hour = Math.floor(totalSeconds / 3600);
    const remainingSeconds = totalSeconds % 3600;
    minute = Math.floor(remainingSeconds / 60);
    sec = remainingSeconds % 60;
    // 补零并返回（小时可能大于10，无需强制补零，如 12:03:45）
    return `${hour}:${minute.toString().padStart(2, "0")}:${sec.toString().padStart(2, "0")}`;
  } else {
    // 仅计算分钟、秒（默认场景）
    minute = Math.floor(totalSeconds / 60);
    sec = totalSeconds % 60;
    // 补零（分钟和秒均为两位数，如 03:05）
    return `${minute.toString().padStart(2, "0")}:${sec.toString().padStart(2, "0")}`;
  }
};