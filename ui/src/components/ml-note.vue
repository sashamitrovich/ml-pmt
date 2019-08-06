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
  props: ['type', 'id'],
  data() {
    if (this.id) {
      crudApi.read(this.type, this.id).then(response => {
        this.note = JSON.parse(response.response);
      });
    }
    return {
      note: this.initNote()
    };
  },
  computed: {
    profile() {
      return this.$store.state.auth.profile || {};
    },
    mode() {
      if (this.id) {
        return 'edit';
      } else {
        return 'create';
      }
    }
  },
  methods: {
    save: function(e) {
      console.log(this.note.text);
      const toast = this.$parent.$parent.$refs.toast;
      var data = this.note;

      // create mode (new note)
      if (this.mode === 'create') {
        return this.$store
          .dispatch('crud/' + this.type + '/create', {
            data,
            format: 'json'
          })
          .then(response => {
            if (response.isError) {
              toast.showToast(response.error, { theme: 'error' });
            } else {
              toast.showToast('Created', { theme: 'success' });
              this.$router.push({
                name: 'root.view',
                params: { id: response.id }
              });
            }
          });
      } else {
        // use update when in update mode (for existing note)
        return this.$store
          .dispatch('crud/' + this.type + '/update', {
            id: this.id,
            data,
            format: 'json'
          })
          .then(response => {
            if (response.isError) {
              toast.showToast(response.error, { theme: 'error' });
            } else {
              toast.showToast('Saved', { theme: 'success' });
              this.$router.push({ name: 'root.view', params: { id: this.id } });
            }
          });
      }
    },
    initNote: function() {
      return {
        text: ''
      };
    }
  }
};
</script>