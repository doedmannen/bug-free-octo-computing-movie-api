<template>
  <div>
    <h1>Nothing here...</h1>
    <p>
      {{ missingPath }} is missing.
    </p>
    <p>
      This might have been caused by: 
      <ul>
        <li>
          Clicking an old link
        </li>
        <li>
          Spelling errors in the url 
        </li>
        <li>
          Aliens invading the server
        </li>
        <li>
          Bad developers
        </li>
      </ul>
    </p>
  </div>
</template>

<script>
export default {
  name: "missing-page",

  components: {
    //
  },

  data: () => ({
    //
  }),
  computed: {
    missingPath() {
      return window.location.pathname; 
    }
  }
};
</script>

<style scoped>
</style>

