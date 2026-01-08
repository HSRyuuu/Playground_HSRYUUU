# 프론트엔드 개발 규칙(Vue3 + Nuxt3)
## [ 선언 순서 ]

### Setup
- Composition API 기반의 <script setup> 에서는 논리적 흐름과 타입/상태/행동의 구분에 따라 선언 순서를 정의합니다.
1. import type
2. import 외부 모듈 / 컴포저블
3. type 선언
4. 매크로 선언
    - defineProps()
    - defineEmits()
    - defineSlots()
    - defineModel()
    - defineOptions()
5. store, inject, use\*\* 등 composable 호출
6. reactive vars
    - ref → reactive
7. plain vars
    - const → let
8. computed
9. watchers
10. method
11. handler
12. lifecycle (onMounted, onBeforeMount 등)

```
<script setup lang="ts">
  // 1. import type
  import type { TMenu } from '@/types/menu';

  // 2. import 외부 모듈 / 컴포저블
  import { useMenuStore } from '@/stores/menu';

  // 3. type 선언
  type TProps = {}

  // 4. 매크로 선언
  const props = defineProps<TProps>();
  const emit = defineEmits<{
    'submit': [string];
  }>();

  // 5. store 호출
  const store = useMenuStore();

  // 6. reactive 상태 정의
  const menu = ref<TMenu | null>(null);

  // 7. plain 상태 정의
  const number = 100;

  // 8. computed
  const menuTitle = computed(() => menu.value?.name);

  // 9. watch
  watch(menu, (val) => {
    console.log('menu changed', val);
  });

  // 10. methods
  const handleClick = () => emit('submit', 'value');

  // 11. 라이프사이클
  onMounted(() => {
    // 초기화
  });
</script>
```

## [ API 호출 규칙 ]

### 백엔드 API 정의 위치 및 방법
1. 백엔드 API 호출은 `@frontend/api` 폴더 아래에 `api.ts` / `type.ts`로 정의한다.
2. API 함수는 `api호출 컴포저블`, `useClientFetch()`, `useAsyncData()`를 이용하여 작성한다.
3. 페이지 경로 도메인에 맞는 적절한 폴더 구조를 찾고, 없으면 새로 만든다.
    - 예시: `frontend/pages/users/xxx.vue`에서 사용하는 API는 `frontend/api/users/api.ts`와 `frontend/api/users/type.ts`에 작성
    - 페이지 경로 `/pages/[domain]/[feature]/...` → API 경로 `/api/[domain]/[feature]/`
4. `api.ts`: API 함수 정의 (api호출 컴포저블 + useClientFetch 조합)
5. `type.ts`: Request/Response, Dto, enum 타입 정의.
    - response와 dto가 동일하게 사용되는 경우 dto를 우선 선언하고, response대신 dto를 사용한다.
    - dto 명명규칙은 TDomainName 으로 하고, dto 명에 'dto'단어를 포함시키지 않는다.

