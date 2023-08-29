<script setup lang="ts">
import {defineModel} from "vue";

const message = defineModel()

</script>

<template>
    <input :value="message" @input="(e: Event & HTMLInputElement) => message = e.target.value"/>
</template>
