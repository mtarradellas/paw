export const requestStatus = {
    PENDING: 0,
    ACCEPTED: 1,
    REJECTED: 2,
    CANCELED: 3,
    SOLD: 4
};

export const requestStatusToString = status => {
    switch (status) {
        case requestStatus.PENDING:
            return 'pending';
        case requestStatus.ACCEPTED:
            return 'accepted';
        case requestStatus.REJECTED:
            return 'rejected';
        case requestStatus.CANCELED:
            return 'canceled';
        case requestStatus.SOLD:
            return 'sold';
    }
};