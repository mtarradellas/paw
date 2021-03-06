
const sleep = time => {
    return new Promise(resolve => {
        setTimeout(resolve, time);
    })
};

const retryTimes = (initialDuration, n) => {
    return initialDuration * (n >= 4 ? Math.pow(4, 2) : Math.pow(n, 2));
};


const DEFAULT_INITIAL_DURATION = 2000;
export const retryFetch = async (fetchFn, initialDuration = DEFAULT_INITIAL_DURATION) => {
    let n = 0;
    while(true){
        try{
            return await fetchFn();
        }catch (e) {
            console.error(e);
            await sleep(retryTimes(initialDuration, n++));
        }
    }
};

export const repeatedFetch = async (fetchFn, time) => {
    await fetchFn();

    setTimeout(() => repeatedFetch(time, fetchFn), time);
};

export const getAuthConfig = jwt => {
    return {
        headers: {
            authorization: "Bearer " + jwt
        }
    };
};