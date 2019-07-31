var app = new Vue({
  el: '#app',
  data: {
    pageResult: null,
    paginationBar: null
  },
  computed: {
    isPageVisible: function() { 
      return this.pageResult && this.pageResult.totalItemsCount > 0
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
        this.loadFavorites(this.pageResult.paginationCriteria.sortingCriteria, dir)
    },
    getPage: function(pageNum){
        this.loadFavorites(
            this.pageResult.paginationCriteria.sortingCriteria,
            this.pageResult.paginationCriteria.sortingDirection,
            pageNum
        )
    },
    //
    loadFavorites: function(sortBy, sortDir, pageNum){
        alert(sortBy + " ## " + sortDir + " ## " + pageNum)
        axios
            .get('/ajax/favorites', {
               params: {
                 sortBy: sortBy || 'Activity',
                 sortDir: sortDir || 'Desc',
                 page: pageNum || 1
               }
            })
            .then(response => {
              this.pageResult = response.data.pageResult
              this.paginationBar = response.data.paginationBar
            })
            .catch(error => {
              console.log(error)
            })
    },
    isSortCriteriaActive: function (criteria) {
      var active = this.pageResult ? this.pageResult.paginationCriteria.sortingCriteria == criteria : false
      return {"has-text-weight-semibold" : active }
    },
    isSortDirectionActive: function(dir) {
      var active = this.pageResult ? this.pageResult.paginationCriteria.sortingDirection == dir : false
      return { "is-info" : active, "is-selected" : active}
    },
    isCurrentPage: function(btn) {
      return { "is-current" : btn.current }
    }
  }
});