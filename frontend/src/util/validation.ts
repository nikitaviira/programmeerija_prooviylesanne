export const personalCodeIsValid = (code: string): boolean =>
  code?.length === 11 && dateIsValid(code) && isControlNumberValid(code);

export const registryCodeIsValid = (code: string): boolean =>
  /^[0-9]{8}$/.test(code) && isControlNumberValid(code);

const isControlNumberValid = (code: string): boolean =>
  calculateControlNumber(code) === parseInt(code.slice(-1), 10);

const calculateControlNumber = (personalCode: string): number => {
  const w1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 1];
  const w2 = [3, 4, 5, 6, 7, 8, 9, 1, 2, 3];
  const digits = personalCode.slice(0, -1).split('').map(Number);

  let controlNumber = calculateSum(digits, w1) % 11;
  if (controlNumber === 10) {
    controlNumber = calculateSum(digits, w2) % 11;
    return controlNumber === 10 ? 0 : controlNumber;
  }
  return controlNumber;
};

const calculateSum = (digits: number[], weights: number[]): number =>
  digits.reduce((sum, digit, index) => sum + digit * weights[index], 0);

const getCenturyPrefix = (identifier: number): string =>
  identifier < 3 ? '18' : identifier < 5 ? '19' : '20';

const dateIsValid = (personalCode: string): boolean => {
  const dateString = personalCode.substring(1, 7);
  const centuryPrefix = getCenturyPrefix(getGenderIdentifier(personalCode));
  const year = `${centuryPrefix}${dateString.substring(0, 2)}`;
  const month = dateString.substring(2, 4);
  const day = dateString.substring(4, 6);
  return !isNaN(new Date(`${year}-${month}-${day}`).getTime());
};

const getGenderIdentifier = (personalCode: string): number =>
  parseInt(personalCode.charAt(0), 10);
