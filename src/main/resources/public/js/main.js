var app = new Vue({
  el: '#app',
  data: {
    loadCompleted: false,
    pageResult: null,
    paginationBar: null,
    sortBy: 'Activity',
    searchKey: null
  },
  computed: {
    isPageEmpty: function() {
      return this.pageResult && this.pageResult.totalItemsCount > 0
    }
  },
  mounted () {
    this.loadFavorites()
  },
  watch: {
    sortBy: function(val){
        this.loadFavorites(
            val,
            null,
            null,
            this.searchKey
        )
    }
  },
  methods: {
    selectSortDirection: function(dir){
        this.loadFavorites(
            this.pageResult.paginationCriteria.sortingCriteria,
            dir,
            null,
            this.searchKey
         )
    },
    getPage: function(pageNum){
        this.loadFavorites(
            this.pageResult.paginationCriteria.sortingCriteria,
            this.pageResult.paginationCriteria.sortingDirection,
            pageNum,
            this.searchKey
        )
    },
    search: function(){
        this.loadFavorites(
            this.pageResult.paginationCriteria.sortingCriteria,
            this.pageResult.paginationCriteria.sortingDirection,
            null,
            this.searchKey
        )
    },
    //
    loadFavorites: function(sortBy, sortDir, pageNum, searchKey){
        // alert(sortBy + " ## " + sortDir + " ## " + pageNum)
        axios
            .get('/ajax/favorites', {
               params: {
                 sortBy: sortBy || 'Activity',
                 sortDir: sortDir || 'Desc',
                 page: pageNum || 1,
                 query : searchKey || ""
               }
            })
            .then(response => {
              this.pageResult = response.data.pageResult
              this.paginationBar = response.data.paginationBar
              this.loadCompleted = true
            })
            .catch(error => {
              console.log(error)
            })
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