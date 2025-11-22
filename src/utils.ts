export const getRandomId = () => {
  return Math.random()
    .toString(36)
    .substring(2, 2 + 10);
};
