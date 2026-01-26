import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const successCount = new Counter('reservation_success');
const failCount = new Counter('reservation_fail');

export const options = {
    scenarios: {
        fcfs_rush: {
            executor: 'shared-iterations',
            vus: 100,
            iterations: 500,
            maxDuration: '30s',
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<1000'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
    const userId = Math.floor(Math.random() * 100000) + 1;
    const eventId = __ENV.EVENT_ID || '1';

    const res = http.post(
        `${BASE_URL}/api/fcfs/reservations`,
        JSON.stringify({ eventId: parseInt(eventId), userId }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    const success = check(res, {
        'status is 200': (r) => r.status === 200,
    });

    if (success) {
        successCount.add(1);
    } else {
        failCount.add(1);
    }

    sleep(0.1);
}
