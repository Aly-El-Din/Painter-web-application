import { createApp } from 'vue';
import App from './App.vue';
import VueKonva from 'vue-konva';
import PrimeVue from 'primevue/config';
import ColorPicker from 'primevue/colorpicker';

const app = createApp(App);
app.use(PrimeVue);
app.use(VueKonva);
app.component('CP', ColorPicker);
app.mount('#app');