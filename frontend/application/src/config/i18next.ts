import i18next, { i18n } from "i18next";

const translator: i18n = i18next.createInstance();

translator
  .init({
    lng: "en",
    fallbackLng: ["en"],
    resources: {
      en: { translation: require("./locales/en.json") }
    }
  })
  .catch((error: Error) => {
    // eslint-disable-next-line no-console
    console.log(error);
  });

export const scopedTranslate = (scope: string) => (
  key: string,
  options?: any
) => translator.t(`${scope}.${key}`, { ...options });

export default translator;
