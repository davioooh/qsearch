var app = new Vue({
  el: '#app',
  data: {
    page: null,
    paginationBar: null
    //
    
    //sortBy: page.paginationCriteria.sortingCriteria,
    //sortDir: page.paginationCriteria.sortingDirection
  },
  computed: {
    isPageVisible: function() { 
      return this.page && this.page.totalItemsCount > 0 
    }
  },
  mounted () {
    axios
    .get('/ajax/favorites')
    .then(response => {
      this.page = response.data.pageResult
      this.paginationBar = response.data.paginationBar
    })
    .catch(error => {
      console.log(error)
    })
  },
  methods: {
    isTabActive: function (tab) {
      console.log(tab +" -- "+ this.page.paginationCriteria.sortingCriteria)
      var active = this.page ? this.page.paginationCriteria.sortingCriteria == tab : false
      return {"has-text-weight-semibold" : active }
    },
    isSortDirActive: function(dir) {
      console.log(dir +" -- "+ this.page.paginationCriteria.sortingDirection)
      var active = this.page ? this.page.paginationCriteria.sortingDirection == dir : false
      return { "is-info" : active, "is-selected" : active}
    },
    isCurrentPage: function(btn) {
      return { "is-current" : btn.current }
    }
  }
});