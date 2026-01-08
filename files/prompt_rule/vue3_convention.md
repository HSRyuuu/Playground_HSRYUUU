
# [ 프론트엔드 코딩 컨벤션 ]
> 프로젝트는 ECMAScript 6 (ES6) 이상의 문법을 기본으로 사용하며,
> 모듈 시스템은 CommonJS(require, module.exports)가 아닌 ESModule(import, export) 방식을 사용합니다.
> ES6의 const, let, arrow function, template literal, destructuring, spread/rest 등 최신 문법을 적극 활용하여 가독성과 유지보수성을 높입니다.

### 기본 규칙
- 들여쓰기: 기본 space 2문자를 사용합니다.
- 문자열: 작은 따옴표를 사용합니다.

###  Module
```
// ❌ Bad
const fs = require('fs');
module.exports = { handler };

// ✅ Good
import fs from 'fs';

export function handler() {
  // ...
}
```

### 변수 선언
var 키워드를 사용하지 않고 const 와 let 을 사용하여 변수를 선언합니다.
```
// ❌ Bad
var num = 1;

// ✅ Good
const num = 1;
let number = 1;
```

### Arrow Function (화살표 함수)
```
const greet = (name: string) => `Hello, ${name}`;
```

### 템플릿 리터럴
```
const message = `총 ${items.length}개의 항목이 있습니다.`;
```

### 구조 분해 할당
```
const { name, age } = user;
const [first, second] = list;
```

### 전개 연산자 (Spread / Rest)
```
const newUser = { ...user, role: 'admin' };
const newArr = [...arr, 6];
```

## [ Naming Convention ]

## Function
### 함수 이름은 기능이 명확히 드러나도록 시멘틱하게 작성합니다.
```
// ❌ Bad
function data() {}

// ✅ Good
function getUserProfile() {}
```
### Boolean 값 반환
- Boolean 값을 반환하는 함수는 is 또는 has, use 같은 접두사를 사용합니다.

```
const isLoggedIn = (): boolean => {
  return !!localStorage.getItem('token');
}

function hasPermission(role: string): boolean {
  return role === 'admin';
}
```

### 데이터 값 반환
- 데이터 값을 반환하는 함수는 get 접두사를 사용합니다.

```
const getUserName = () => {
  return '홍길동';
}
```
### 이벤트 핸들러
- 컴포넌트 내부에서 정의된 이벤트 핸들러 함수는 handle 접두사를 사용합니다.

```
<template>
  <button @click="handleClick">클릭</button>
</template>

<script setup lang="ts">
const handleClick = (event: MouseEvent) => {
  console.log('클릭됨!', event);
}
</script>
```
### 외부 props 전달 핸들러
- 외부로부터 props로 전달되는 이벤트 핸들러는 on 접두사를 사용합니다.
```
// 부모 컴포넌트
<ChildComponent :onSubmit="handleSubmit" />

<script setup>
  const handleSubmit = (formData: any) => {
    console.log('제출됨', formData);
  }
</script>
```

```
<!-- 자식 컴포넌트 -->
<template>
  <button @click="emitSubmit">제출</button>
</template>

<script setup lang="ts">
  const props = defineProps<{
    onSubmit: (data: string) => void;
  }>();

  const emitSubmit = () => {
    props.onSubmit?.('폼 데이터');
  }
</script>
```

### Type
- PascalCase 를 사용합니다.
- 접두사로 T를 붙입니다.

```
export type TUserId = number;

export type TProps = {
  userId: number;
};
```

### Interface
- PascalCase 를 사용합니다.
- 접두사로 I를 붙입니다.

```
export type IUserId = number;

export type IProps = {
  userId: number;
};
```

### Enum
- 키는 PascalCase 를 사용합니다.
- 값은 일반적으로 상수처럼 UPPER_SNAKE_CASE를 사용합니다. (필요에 따라 다르게 선언 가능합니다)

```
export enum Service {
  Public = 'PUBLIC',
  Management = 'MANAGEMENT',
}
```

### Style
- 클래스명은 BEM(Block–Element–Modifier) 규칙을 따릅니다.
    - Block: 독립적인 컴포넌트 단위
    - Element: Block 내부의 구성 요소
    - Modifier: 상태나 변형을 표현

```
<!-- 'card'는 Block, 'card__title'은 Element, 'card--active'는 Modifier -->

<template>
  <div class="card card--active">
    <h3 class="card__title">타이틀</h3>
    <p class="card__content">내용</p>
  </div>
</template>
```

```
<!-- 클래스명은 소문자와 숫자로 구성하고, 단어 구분은 -(하이픈)으로 연결합니다. -->

<template>
  <div class="login-form">
    <label class="login-form__label">아이디</label>
    <input class="login-form__input" />
    <p class="login-form__message login-form__message--error">에러 메시지</p>
  </div>
</template>
```

---

## [ 배열과 객체 ]

선언시 리터럴 구문을 사용합니다.

```
// ❌ Bad
const arr = new Array(1, 2, 3);
const obj = new Object();

// ✅ Good
const arr = [1, 2, 3];
const obj = {};
```

### 전개 연산자(Spread Operator) 사용

- for 루프를 사용해 배열을 수동으로 복사하지 않고, [...] 전개 연산자 또는 .slice() 같은 내장 메서드를 사용합니다.
- 객체 복사도 전개 연산자를 사용합니다.

```
const items = [1, 2, 3, 4];

// ❌ Bad
const itemsCopy = [];
const len = items.length;

for (let i = 0; i < len; i++) {
  itemsCopy[i] = items[i];
}

// ✅ Good
const itemsCopy = [...items]; // spread 사용
const shallowCopy = items.slice(); // 또는 slice도 가능
```

```
const user = { name: 'Lee', age: 25 };
const userCopy = { ...user };
```

---

# [ Directory Structure ]

디렉토리별 역할과 구성에 대한 자세한 내용은 아래에서 확인하실 수 있습니다.

https://nuxt.com/docs/guide/directory-structure/nuxt

## Pages

- 웹 애플리케이션의 라우트를 생성할 때는 파일 기반 라우팅을 사용합니다.
- 디렉토리 및 파일은 kebab-case 로 작성합니다.
- 디렉토리 하위에 index.vue 또는 필요한 경우 index.scss 를 생성합니다.

```
pages/
└── user-profile/
    ├── index.vue ✅
    └── index.scss ✅ (선택)
```

## Components

- Atomic Design 패턴을 기반으로 구조화합니다.
- 컴포넌트를 감싸는 그룹 디렉토리(예: atom, molecule, organism)는 kebab-case로 작성합니다.
- 컴포넌트 디렉토리 및 파일은 PascalCase로 작성합니다.
- 디렉토리 하위에 동일한 이름의 .vue 파일을 생성하고, 필요한 경우 같은 이름의 .scss 파일도 함께 생성합니다.
- 참고: https://fe-developers.kakaoent.com/2022/220505-how-page-part-use-atomic-design-system/

```
components/
└── atom/
    └── BaseButton/
        ├── BaseButton.vue ✅
        └── BaseButton.scss ✅ (선택)
└── molecule/
    └── FormGroup/
        ├── FormGroup.vue
        └── FormGroup.scss
```

## Composables

- 파일 이름은 camelCase로 작성합니다.
- 접두사로 use를 붙입니다.

## Api, Assets, Constants, Layouts, Middleware, Plugins, Stores

- 디렉토리 및 파일은 kebab-case로 작성합니다

---

### 레퍼런스
Nuxt 3 & Vue 3
https://nuxt.com/
https://ko.vuejs.org/