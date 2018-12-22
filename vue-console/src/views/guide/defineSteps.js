const steps = [
  {
    element: '.hamburger-container',
    popover: {
      title: '首页',
      description: '这里可以看见爬虫爬取的数据统计信息以及错误日志',
      position: 'right'
    }
  },
  {
    element: '.breadcrumb-container',
    popover: {
      title: 'Breadcrumb',
      description: 'Indicate the current page location',
      position: 'left'
    }
  },
  {
    element: '.screenfull',
    popover: {
      title: 'Screenfull',
      description: 'Bring the page into fullscreen',
      position: 'left'
    }
  },
  {
    element: '.international-icon',
    popover: {
      title: 'Switch language',
      description: 'Switch the system language',
      position: 'left'
    }
  },
  {
    element: '.theme-switch',
    popover: {
      title: 'Theme Switch',
      description: 'Custom switch system theme',
      position: 'left'
    }
  },
  {
    element: '.tags-view-container',
    popover: {
      title: 'Tags view',
      description: 'The history of the page you visited',
      position: 'bottom'
    },
    padding: 0
  }
]

export default steps
