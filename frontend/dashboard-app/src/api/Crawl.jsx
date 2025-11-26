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

    crawlHistory: (page) => {
        return axiosCrawl.get('crawl-history', {
            params: {page},
            requireAuth: true
        });
    }
}
export default CrawlApi;