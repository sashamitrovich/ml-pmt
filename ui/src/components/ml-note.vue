<template>
  <form @submit.prevent="save">
    <div>
      <textarea v-model="note.text" placeholder="add multiple lines"></textarea>
    </div>
    <div class="field is-grouped">
      <div class="control">
        <button class="button is-primary">Submit</button>
      </div>
    </div>
  </form>
</template>

<script>
export default {
  name: 'ml-note',
  data() {
    return {
      note: {
        text:''    
      } 
    };
  },
  methods: {
      save: function (e) {
          // logging only, for now
          console.log(this.note.text);      
          
      }
  }
};
</script>