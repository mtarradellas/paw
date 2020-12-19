export const petStatus = {
    AVAILABLE: 0,
    REMOVED: 1,
    SOLD: 2,
    UNAVAILABLE: 3
};

export const petStatusToString = status => {
    switch (status) {
        case petStatus.AVAILABLE:
            return 'available';
        case petStatus.REMOVED:
            return 'removed';
        case petStatus.SOLD:
            return 'sold';
        case petStatus.UNAVAILABLE:
            return 'unavailable';
    }
};
