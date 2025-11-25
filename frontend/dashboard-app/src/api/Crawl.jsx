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

    crawlLastTime: () => {
        return axiosCrawl.get('crawl-last-time', {
            requireAuth: true
        });
    },

    crawlHistory: () => {
        return axiosCrawl.get('crawl-history', {
            requireAuth: true
        });
    }
}
export default CrawlApi;