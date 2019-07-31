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
    this.loadFavorites()
  },
  methods: {
    selectSortCriteria: function(criteria){
        this.loadFavorites(criteria)
    },
    selectSortDirection: function(dir){
        this.loadFavorites(this.page.paginationCriteria.sortingCriteria, dir)
    },

    //
    loadFavorites: function(sortBy, sortDir){
     axios
        .get('/ajax/favorites', {
           params: {
             sortBy: sortBy || 'Activity',
             sortDir: sortDir || 'Desc'
           }
        })
        .then(response => {
          this.page = response.data.pageResult
          this.paginationBar = response.data.paginationBar
        })
        .catch(error => {
          console.log(error)
        })
    },
    isSortCriteriaActive: function (criteria) {
      var active = this.page ? this.page.paginationCriteria.sortingCriteria == criteria : false
      return {"has-text-weight-semibold" : active }
    },
    isSortDirectionActive: function(dir) {
      var active = this.page ? this.page.paginationCriteria.sortingDirection == dir : false
      return { "is-info" : active, "is-selected" : active}
    },
    isCurrentPage: function(btn) {
      return { "is-current" : btn.current }
    }
  }
});