import axiosCrawl from "./AxiosCrawl";

const CrawlApi = {
    crawlComic: () => {
        return axiosCrawl.get('crawl', {
            requireAuth: true
        });
    },

    crawlComicByOriginName: (originName) => {
        return axiosCrawl.get(`crawl/${originName}`, {
            requireAuth: true
        });
    },
}
export default CrawlApi;